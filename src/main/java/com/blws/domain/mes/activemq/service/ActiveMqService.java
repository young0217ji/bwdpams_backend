package com.blws.domain.mes.activemq.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.blws.domain.mes.activemq.vo.ActiveMqDataManageRequestBodyVO;
import com.blws.domain.mes.activemq.vo.ActiveMqDataMangeRequestDataListVO;
import com.blws.domain.mes.activemq.vo.ActiveMqGetQueryRequestBodyVO;
import com.blws.domain.mes.activemq.vo.ActiveMqGetQueryRequestHeaderVO;
import com.blws.domain.mes.activemq.vo.ActiveMqGetQueryRequestVO;
import com.blws.domain.mes.activemq.vo.ActiveMqGetQueryResultVO;
import com.blws.global.error.exception.BusinessException;
import com.blws.global.error.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ActiveMqService {

	@Autowired
	@Qualifier("JmsTemplate")
	private JmsTemplate jmsTemplate;
	@Autowired
	@Qualifier("JmsSession")
	private ActiveMQSession jmsSession;
	@Autowired
	@Qualifier("ReceiverActiveMQConnectionFactory")
	private ActiveMQConnectionFactory receiverActiveMQConnectionFactory;
	private final String topic;
	private final String subject;
	private final String prefixClientId;
	private final Long receiveTimeOut;
	ActiveMQConnection connection;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public ActiveMqService(@Value("${spring.activemq.jms.template.default-destination}") String topic
			, @Value("${spring.activemq.jms.template.subject}") String subject
			, @Value("${spring.activemq.jms.template.prefix-client-id}") String prefixClientId
			, @Value("${spring.activemq.jms.template.receive-timeout}") Long receiveTimeOut){
		this.topic = topic;
		this.subject = subject;
		this.prefixClientId = prefixClientId;
		this.receiveTimeOut = receiveTimeOut;
	}

	public ActiveMqGetQueryResultVO dataSendAndRecive(ActiveMqDataManageRequestBodyVO body) throws JMSException, IOException {

		ActiveMQSession session = createSession();

		String clientId = this.prefixClientId + UUID.randomUUID().toString().substring(0,6);

		MessageCreator messageCreator = messageCreator(clientId,body,"M");

		//TemporaryTopic responseTopic = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;

		jmsTemplate.setReceiveTimeout(receiveTimeOut);

		Long timeout = receiveTimeOut;
		Destination destination = session.createTopic(topic);

		Message receiveMessage;
		Object returnMessage;

		try {

			Message requestMessage = messageCreator.createMessage(session);
			producer = session.createProducer(destination);
			consumer = session.createConsumer(destination, "subject='" + clientId + "'");

			if (this.log.isDebugEnabled()) {
				this.log.debug("Sending created message: " + requestMessage);
			}

			producer.send(requestMessage);

			if (timeout > 0L) {
				receiveMessage = consumer.receive(timeout);
			} else {
				receiveMessage = timeout < 0L ? consumer.receiveNoWait() : consumer.receive();
			}

			SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

			returnMessage =  simpleMessageConverter.fromMessage(receiveMessage);

		} finally {
			closeConnection(consumer,producer,session);
		}

		XmlMapper xmlMapper = new XmlMapper();
		ActiveMqGetQueryResultVO activeMqGetQueryReply = xmlMapper.readValue((byte[]) returnMessage, new TypeReference<ActiveMqGetQueryResultVO<HashMap<String, Object>>>() {});

		// [2023.07.27-구자윤] MES Reply Message의 오류 Return 사용자 알림 적용
		//        this.checkReturnMap(activeMqGetQueryReply.getReturn());

		return activeMqGetQueryReply;
	}

	//    public List<ActiveMqGetQueryResultDataVO> getQueryResult(ActiveMqGetQueryRequestBodyVO body) throws JMSException, IOException {
	//
	//
	//        Session session= createSession();
	//
	//        String clientId = this.prefixClientId + UUID.randomUUID().toString().substring(0,6);
	//
	//        MessageCreator messageCreator = messageCreator(clientId,body,"GetQueryResult");
	//
	//        //TemporaryTopic responseTopic = null;
	//        MessageProducer producer = null;
	//        MessageConsumer consumer = null;
	//
	//        jmsTemplate.setReceiveTimeout(receiveTimeOut);
	//
	//        Long timeout = receiveTimeOut;
	//        Destination destination = session.createTopic(topic);
	//
	//        Message receiveMessage;
	//        Object returnMessage;
	//
	//        try {
	//
	//            Message requestMessage = messageCreator.createMessage(session);
	//            producer = session.createProducer(destination);
	//            consumer = session.createConsumer(destination, "subject='" + clientId + "'");
	//
	//            if (this.log.isDebugEnabled()) {
	//                this.log.debug("Sending created message: " + requestMessage);
	//            }
	//
	//            producer.send(requestMessage);
	//
	//            if (timeout > 0L) {
	//                receiveMessage = consumer.receive(timeout);
	//            } else {
	//                receiveMessage = timeout < 0L ? consumer.receiveNoWait() : consumer.receive();
	//            }
	//
	//            SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
	//
	//            returnMessage =  simpleMessageConverter.fromMessage(receiveMessage);
	//
	//        } finally {
	//            closeConnection(consumer,producer,session);
	//        }
	//
	//        XmlMapper xmlMapper = new XmlMapper();
	//        ActiveMqGetQueryResultVO activeMqGetQueryResultVO = xmlMapper.readValue((byte[]) returnMessage, new TypeReference<ActiveMqGetQueryResultVO<HashMap<String, Object>>>(){});
	//
	//        this.checkReturnMap(activeMqGetQueryResultVO.getReturn());
	//
	//        if(activeMqGetQueryResultVO.getBody() != null){
	//            return (List<ActiveMqGetQueryResultDataVO>) activeMqGetQueryResultVO.getBody().getData();
	//        }else{
	//            return new ArrayList<>();
	//        }
	//    }

	public Map<String, Object> getQueryResult(ActiveMqGetQueryRequestBodyVO body) throws JMSException, IOException {


		Session session= createSession();

		String clientId = this.prefixClientId + UUID.randomUUID().toString().substring(0,6);

		MessageCreator messageCreator = messageCreator(clientId,body,"GetQueryResult");

		//TemporaryTopic responseTopic = null;
		MessageProducer producer = null;
		MessageConsumer consumer = null;

		jmsTemplate.setReceiveTimeout(receiveTimeOut);

		Long timeout = receiveTimeOut;
		Destination destination = session.createTopic(topic);

		Message receiveMessage;
		Object returnMessage;

		try {

			Message requestMessage = messageCreator.createMessage(session);
			producer = session.createProducer(destination);
			consumer = session.createConsumer(destination, "subject='" + clientId + "'");

			if (this.log.isDebugEnabled()) {
				this.log.debug("Sending created message: " + requestMessage);
			}

			producer.send(requestMessage);

			if (timeout > 0L) {
				receiveMessage = consumer.receive(timeout);
			} else {
				receiveMessage = timeout < 0L ? consumer.receiveNoWait() : consumer.receive();
			}

			SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();

			returnMessage =  simpleMessageConverter.fromMessage(receiveMessage);

		} finally {
			closeConnection(consumer,producer,session);
		}

		XmlMapper xmlMapper = new XmlMapper();
		ActiveMqGetQueryResultVO activeMqGetQueryResultVO = xmlMapper.readValue((byte[]) returnMessage, new TypeReference<ActiveMqGetQueryResultVO<HashMap<String, Object>>>(){});

		Map<String, Object> oReturnMap = new HashMap<String, Object>();
		if ( activeMqGetQueryResultVO.getBody() != null ) {
			oReturnMap.put("body", activeMqGetQueryResultVO.getBody().getData());
		}
		else {
			oReturnMap.put("body", null);
		}
		oReturnMap.put("returncode", activeMqGetQueryResultVO.getReturn().get("returncode"));
		oReturnMap.put("returntype", activeMqGetQueryResultVO.getReturn().get("returntype"));
		oReturnMap.put("returnmessage", activeMqGetQueryResultVO.getReturn().get("returnmessage"));
		oReturnMap.put("returndetailmessage", activeMqGetQueryResultVO.getReturn().get("returndetailmessage"));

		return oReturnMap;
	}

	private MessageCreator messageCreator(String clientId, Object body, String messageName) {
		return new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {

				XmlMapper xmlMapper = new XmlMapper();
				xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
				xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, false);

				ActiveMqGetQueryRequestVO request = null;
				Map<String, Object> map = new HashMap<>();

				String userId = SecurityContextHolder.getContext().getAuthentication().getName();

				switch (messageName){
				case "GetQueryResult":

					ActiveMqGetQueryRequestBodyVO activeMqGetQueryRequestBodyVO = (ActiveMqGetQueryRequestBodyVO) body;
					activeMqGetQueryRequestBodyVO.setEventuser(userId);

					map = new HashMap<>();
					map.put("FACTORYID", activeMqGetQueryRequestBodyVO.getFactoryid());
					map.put("QUERYID", activeMqGetQueryRequestBodyVO.getQueryid());
					map.put("QUERYVERSION", activeMqGetQueryRequestBodyVO.getQueryversion());
					map.put("LANGUAGE", activeMqGetQueryRequestBodyVO.getLanguage());
					map.put("EVENTUSER", activeMqGetQueryRequestBodyVO.getEventuser());

					Object oBindV = activeMqGetQueryRequestBodyVO.getBindv();
					if ( oBindV != null ) {

						ObjectMapper objMapper = new ObjectMapper();
						Map<String, Object> mBindMap = new HashMap<String, Object>();
						Map<String, Object> mConvertBindMap = new HashMap<String, Object>();

						try {
							mBindMap = objMapper.readValue(oBindV.toString(), new TypeReference<Map<String, Object>>(){});
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if ( mBindMap != null && mBindMap.size() > 0 ) {
							Iterator iterMap = mBindMap.entrySet().iterator();
							while (iterMap.hasNext()) {
								Entry oEntry = (Entry) iterMap.next();
								String sKey = (String) oEntry.getKey();
								Object oValue = mBindMap.get(sKey);

								mConvertBindMap.put(sKey.toUpperCase(), oValue);
							}

							map.put("BINDV", mConvertBindMap);
						}
						else {
							map.put("BINDV", null);
						}
					}
					else {
						map.put("BINDV", null);
					}

					request = ActiveMqGetQueryRequestVO.builder().header(ActiveMqGetQueryRequestHeaderVO.builder()
							.messagename(messageName)
							.replysubject(clientId)
							.sourcesubject(clientId)
							.targetsubject(subject)
							.transactionid(clientId).build()
							).body(map).build();
					break;

				case "M":

					// [2023.08.10-구자윤] MES Message 동적 Element 구성을 위한 로직 변경
					ActiveMqDataManageRequestBodyVO activeMqDataManageRequestBodyVO = (ActiveMqDataManageRequestBodyVO) body;

					map = new HashMap<>();

					String messageName = activeMqDataManageRequestBodyVO.getMessagename();
					String factoryid = activeMqDataManageRequestBodyVO.getFactoryid();
					String processRouteId = activeMqDataManageRequestBodyVO.getProcessrouteid();

					Object oParamV = activeMqDataManageRequestBodyVO.getParamv();
					if ( oParamV != null ) {

						ObjectMapper objMapper = new ObjectMapper();
						Map<String, Object> mBindMap = new HashMap<String, Object>();

						try {
							String json = objMapper.writeValueAsString(oParamV);
							mBindMap = objMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
						} catch (JsonMappingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if ( mBindMap != null && mBindMap.size() > 0 ) {
							Iterator iterMap = mBindMap.entrySet().iterator();
							while (iterMap.hasNext()) {
								Entry oEntry = (Entry) iterMap.next();
								String sKey = (String) oEntry.getKey();
								Object oValue = mBindMap.get(sKey);

								map.put(sKey.toUpperCase(), oValue);
							}
						}
					}

					String evsentUser = userId;

					// User 이벤트인 경우 Password 암호화 진행
					if ( "TxnSFUser".equalsIgnoreCase(messageName) ) {
						ActiveMqDataMangeRequestDataListVO oDataInfo = ((ActiveMqDataManageRequestBodyVO) body).getDatalist();
						Set<Map<String, Object>> otemp = oDataInfo.getDatainfo();

						for( Iterator iterator = otemp.iterator(); iterator.hasNext(); )
						{
							Map<String,Object> mDataInfoMap = (Map<String, Object>) iterator.next();
							if ( mDataInfoMap != null && mDataInfoMap.size() > 0 && mDataInfoMap.containsKey("NEWPASSWORD") ) {

								Object oPasswod = mDataInfoMap.get("NEWPASSWORD");
								if ( oPasswod != null && oPasswod.toString().isEmpty() == false ) {
									oPasswod = passwordEncoder.encode(oPasswod.toString());
									mDataInfoMap.put("NEWPASSWORD", oPasswod);
								}
							}
						}
					}

					map.put("EVENTUSER",evsentUser);
					map.put("FACTORY",factoryid);
					map.put("PROCESSROUTEID",processRouteId);
					map.put("DATALIST",((ActiveMqDataManageRequestBodyVO) body).getDatalist());

					request = ActiveMqGetQueryRequestVO.builder().header(ActiveMqGetQueryRequestHeaderVO.builder()
							.messagename(messageName)
							.replysubject(clientId)
							.sourcesubject(clientId)
							.targetsubject(subject)
							.transactionid(clientId).build()
							).body(map).build();
					break;

				}

				String messagestr = null;

				try {
					messagestr = xmlMapper.writeValueAsString(request);
					log.info("message == {}", messagestr);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}

				TextMessage message = session.createTextMessage(messagestr);
				message.setStringProperty("transactionid", UUID.randomUUID().toString());
				message.setStringProperty("subject", subject);
				message.setJMSCorrelationID(clientId);
				message.setStringProperty("replysubject", clientId);
				jmsTemplate.setDefaultDestinationName(topic);

				return message;
			}
		};
	}

	private ActiveMQSession createSession() throws JMSException {
		if(jmsSession.isClosed()){
			ActiveMQConnection connection = (ActiveMQConnection) receiverActiveMQConnectionFactory.createConnection();
			connection.start();
			jmsSession = (ActiveMQSession) connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		}
		return jmsSession;
	}


	private void closeConnection(MessageConsumer consumer,MessageProducer producer,Session session) throws JMSException {
		JmsUtils.closeMessageConsumer(consumer);
		JmsUtils.closeMessageProducer(producer);
	}

	private void checkReturnMap(Map<String,Object> returnMap) {
		if("9999".equals(returnMap.get("returncode"))){
			throw new BusinessException(ErrorCode.METHOD_NOT_ALLOWED);
		}
	}
}