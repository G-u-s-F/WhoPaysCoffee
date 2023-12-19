package ioc.codemugteam.whopayscoffee;

import java.util.Date;

public class Pago {
    private int paymentId, memberId, groupId;
    private float amount;
    private String memberName;
    private Date paymentDate;

    public Pago(int paymentId, int memberId, int groupId, float amount, String memberName, Date paymentDate) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.groupId = groupId;
        this.amount = amount;
        this.memberName = memberName;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
