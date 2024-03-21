package co.saiyan.common.doc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger配置
 * @author larry
 */
@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

	/**
	 * 是否开启swagger
	 */
	private Boolean enabled = true;

	/**
	 * 应用名
	 **/
	private String appname = "";

	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 标题
	 **/
	private String title = "";
}
