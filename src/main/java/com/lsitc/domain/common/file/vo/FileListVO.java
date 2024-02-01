package com.lsitc.domain.common.file.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileListVO {
	private List<FileVO> fileList;
}
