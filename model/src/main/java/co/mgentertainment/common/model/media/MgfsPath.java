package co.mgentertainment.common.model.media;

/**
 * @author larry
 * @createTime 2023/10/31
 * @description MgfsPath
 */
public interface MgfsPath {

    String ROOT_PATH = "/data/mgfs";
    String MAIN_PATH = ROOT_PATH + "/main";
    String VICE_PATH = ROOT_PATH + "/vice";
    String MZK_PATH = "/tmp/mzk";

    enum MgfsPathType {
        MAIN(MAIN_PATH),
        VICE(VICE_PATH),
        MZK(MZK_PATH);

        private String value;

        MgfsPathType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
