package co.saiyan.common.fileupload.aspect;

import co.saiyan.common.fileupload.FileInfo;
import co.saiyan.common.fileupload.platform.FileStorage;

/**
 * 获取文件的访问控制列表调用链结束回调
 */
public interface SetFileAclAspectChainCallback {
    boolean run(FileInfo fileInfo, Object acl, FileStorage fileStorage);
}
