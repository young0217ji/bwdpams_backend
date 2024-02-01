package com.lsitc.domain.common.file.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lsitc.domain.common.file.vo.FileListVO;
import com.lsitc.domain.common.file.vo.FileVO;

@Service
public class FileService {
	public Map<String, Object> uploadFile(FileListVO fileListVO) {
		Map<String,Object> result = new HashMap<>();
		List<FileVO> fileList = fileListVO.getFileList();
		for(FileVO file : fileList) {
			String orgFileName = file.getFileName();
			String fileLocation = file.getFileLocation();
			
			MultipartFile getFile = file.getFile();
			
			File uploadFile = new File(fileLocation+orgFileName);
			uploadFile.mkdirs();
			System.out.println("savedName : "+uploadFile.getPath());
			try {
				getFile.transferTo(uploadFile);
				result.put("returncode", "0");
			} catch (IOException e) {
				result.put("returncode", "error");
				result.put("returnmessage", "파일 업로드 중 오류 발생");
			}	
		}
		return result;
	}
	
	public void downloadFile(HttpServletResponse response, String fileName, String fileLocation) throws IOException{
		
		byte[] fileByte = FileUtils.readFileToByteArray(new File(fileLocation+"/"+fileName));
		
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; fileName=\""+URLEncoder.encode(fileName, "UTF-8")+"\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
