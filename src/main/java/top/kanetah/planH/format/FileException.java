package top.kanetah.planH.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "服务端无法处理文件")
class FileException extends RuntimeException {
}

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "文件类型错误")
class FileTypeException extends RuntimeException {
}

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "无效的压缩文件")
class CompactFileException extends RuntimeException {
}

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "文件夹内容格式错误")
class DirectoryContentException extends RuntimeException {
}

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "文件命名格式错误")
class FileNameException extends RuntimeException {
}
