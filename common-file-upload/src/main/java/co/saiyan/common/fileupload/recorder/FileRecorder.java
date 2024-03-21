package co.saiyan.common.fileupload.recorder;

import co.saiyan.common.fileupload.FileInfo;

/**
 * 文件记录记录者接口
 */
public interface FileRecorder {

    /**
     * 保存文件记录
     */
    boolean save(FileInfo fileInfo);

    /**
     * 根据 url 获取文件记录
     */
    FileInfo getByUrl(String url);

    /**
     * 根据 url 删除文件记录
     */
    boolean delete(String url);
}
