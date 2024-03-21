package co.saiyan.common.model.media;

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
    String MZK_SV_PATH = "/tmp/sv_mzk";

    enum MgfsPathType {
        MAIN(MAIN_PATH),
        VICE(VICE_PATH),
        MZK(MZK_PATH),
        MZK_SHORT(MZK_SV_PATH);

        private String value;

        MgfsPathType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
