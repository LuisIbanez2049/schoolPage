package com.eschool.schoolpage.dtos;

import java.util.List;

public record RecordAddFilesToContent(Long contentId, List<FileObject> fileObjectList) {
}
