package com.blws.domain.common.file.vo;


import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {
	private MultipartFile file;
	private String fileName;
	private String fileLocation;
	private String timestamp;
	
}
