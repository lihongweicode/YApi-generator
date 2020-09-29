package org.starr.yapi.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lhw
 * @description
 * @date 2020/8/24
 */
@Data
public class PayApplyVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 费用类型 (1.预付款；2.货款；)
     */
    private Integer costType;
    /**
     * 应付金额
     */
    private Integer payableAmount;
    /**
     * 收款账户类型（1：个人; 2：公司账户）
     */
    private Integer accountType;
    /**
     * 收款账户id
     */
    private Integer accountId;
    /**
     * 账户名称（收款）
     */
    private String payee;
    /**
     * 账户手机号（收款）
     */
    private String payeePhone;
    /**
     * 收款账户
     */
    private String payeeAccount;
    /**
     * 所属银行
     */
    private String bank;
    /**
     * 分行网点
     */
    private String branch;
    /**
     * 联系人名称
     */
    private String receiptName;
    /**
     * 联系人手机号
     */
    private String receiptPhone;
    /**
     * 联系人id
     */
    private Integer receiptId;
    /**
     * 是否有发票(0:没有；1：有)
     */
    private Integer invoice;
}
