package generator;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * biz_number
 * @author 
 */
@Data
public class BizNumber implements Serializable {
    private Long id;

    /**
     * 业务类型
     */
    private String type;

    /**
     * 编号值
     */
    private Long value;

    /**
     * 备注
     */
    private String memo;

    /**
     * 版本号
     */
    private String version;

    /**
     * 修改时间
     */
    private Date modified;

    /**
     * 创建时间
     */
    private Date created;

    private static final long serialVersionUID = 1L;
}