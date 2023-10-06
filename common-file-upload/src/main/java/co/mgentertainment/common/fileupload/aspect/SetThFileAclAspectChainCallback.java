package co.mgentertainment.common.fileupload.aspect;

import co.mgentertainment.common.fileupload.FileInfo;
import co.mgentertainment.common.fileupload.platform.FileStorage;

/**
 * 设置缩略图文件的访问控制列表调用链结束回调
 */
public interface SetThFileAclAspectChainCallback {
    boolean run(FileInfo fileInfo, Object acl, FileStorage fileStorage);
}
