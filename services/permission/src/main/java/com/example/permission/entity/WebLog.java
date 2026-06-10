//package cn.edu.gcu.network.signin.entity;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import lombok.experimental.Accessors;
//
///**
// * <p>
// *
// * </p>
// *
// * @author author
// * @since 2023-12-23
// */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@TableName("WebLog")
//public class WebLog implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @TableId
//    private int id;
//    @TableField("oper_name")
//    private String operName;
//    @TableField("method")
//    private String method;
//    @TableField("url")
//    private String url;
//    @TableField("ip")
//    private String ip;
//    @TableField("status")
//    private int status;
//    @TableField("error_Msg")
//    private String errorMsg;
//    @TableField("oper_time")
//    private Date operTime;
//}
