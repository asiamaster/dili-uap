package com.dili.uap.sdk.session;

import com.dili.uap.sdk.util.ManageRedisUtil;
import com.dili.uap.sdk.util.WebContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 权限配置信息<BR>
 * 优先读取conf/manage-${spring.profiles.active}.properties<BR>
 * 找不到就读取conf/manage.properties<BR>
 * Created by asiamaster on 2017/7/4 0004.
 */
@Component
//@ConfigurationProperties(prefix = "manage",locations = {"classpath:conf/manage-${spring.profiles.active}.properties"})
@ConfigurationProperties(prefix = "manage")
@PropertySource({"classpath:conf/manage.properties"})
public class ManageConfig {

//	@Value("${manage.domain}")
	private String domain;
	//是否开启过滤
//	@Value("${manage.enable}")
	private Boolean enable = true;

	//包含列表
//	@Value("#{'${manage.includes}'.split(',')}")
	private List<String> includes;

	//排除列表
//	@Value("#{'${manage.excludes}'.split(',')}")
	private List<String> excludes;

	//仅作登录验证
	private List<String> loginChecks;

	//profile扩展配置
	private List<String> includesExt;
	private List<String> excludesExt;
	private List<String> loginChecksExt;


	//是否必须在框架内
//	@Value("${manage.mustIframe}")
	private Boolean mustIframe = true;

	private Boolean userLimitOne = false;

	@Autowired
	private ManageRedisUtil redisUtil;

	/**
	 * 是否包含
	 * @return
	 */
	private boolean isInclude(){
		if(WebContent.getRequest() == null){
			return true;
		}
		return urlFilter(includes);
	}
	/**
	 * 是否排除
	 * @return
	 */
	private boolean isExclude(){
		if(WebContent.getRequest() == null){
			return false;
		}
		return urlFilter(excludes);
	}

	private Boolean urlFilter(List<String> patternStrs){
		String uri = WebContent.getRequest().getRequestURI();
		for(String str : patternStrs){
			if(Pattern.compile(str).matcher(uri).find()){
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查是要作登录验证的.action请求，如果该请求url是需要作登录验证的，返回true
	 * @return
	 */
	public boolean isLoginCheck(){
		//如果没有登录请求，则无法取出url，不进行验证
		if(WebContent.getRequest() == null || loginChecks == null){
			return false;
		}
		return urlFilter(loginChecks);
	}

	/**
	 * 验证权限是否通过
	 * @return
	 */
	public boolean hasChecked() {
		if(!enable){
			return false;
		}
		if (isExclude()) {
			return false;
		}
		if (isInclude()) {
			return true;
		}
		return false;
	}

	public String getDomain() {
		return domain;
	}

	public ManageRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public Boolean getEnable() {
		return enable;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

	public Boolean getMustIframe() {
		return mustIframe;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public void setMustIframe(Boolean mustIframe) {
		this.mustIframe = mustIframe;
	}

	public Boolean getUserLimitOne() {
		return userLimitOne;
	}

	public void setUserLimitOne(Boolean userLimitOne) {
		this.userLimitOne = userLimitOne;
	}

	public List<String> getLoginChecks() {
		return loginChecks;
	}

	public void setLoginChecks(List<String> loginChecks) {
		this.loginChecks = loginChecks;
	}

	public List<String> getIncludesExt() {
		return includesExt;
	}

	public void setIncludesExt(List<String> includesExt) {
		this.includesExt = includesExt;
	}

	public List<String> getExcludesExt() {
		return excludesExt;
	}

	public void setExcludesExt(List<String> excludesExt) {
		this.excludesExt = excludesExt;
	}

	public List<String> getLoginChecksExt() {
		return loginChecksExt;
	}

	public void setLoginChecksExt(List<String> loginChecksExt) {
		this.loginChecksExt = loginChecksExt;
	}
}