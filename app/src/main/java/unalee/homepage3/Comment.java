package unalee.homepage3;

import java.io.Serializable;

public class Comment implements Serializable{
    private int customerId;
    private String commentDate;
    private String commentDetail;
    private  int star;

    public Comment(int customerId, String commentDate, int star, String commentDeatil){
        this.customerId = customerId;
        this.commentDate = commentDate;
        this.commentDetail = commentDeatil;
        this.star = star;
    }

//    @Override
//    public boolean equals(Object obj){
//        return this.customerId == ((Customer) obj).customerId;
//    }

    public int getCustomerId(){
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
