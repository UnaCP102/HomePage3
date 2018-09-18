package unalee.homepage3;

import java.io.Serializable;

public class Rating implements Serializable{
    private int IdRating, IdRoomReservation;
    private float ratingStar;
    private String opinion, review;


    public Rating(int IdRating, float ratingStar, String opinion, String review,
                  int IdRoomReservation) {
        this.IdRating = IdRating;
        this.ratingStar = ratingStar;
        this.opinion = opinion;
        this.review = review;
        this.IdRoomReservation = IdRoomReservation;
    }

    public Rating (int IdRoomReservation, float ratingStar, String opinion){
        this.opinion = opinion;
        this.IdRoomReservation = IdRoomReservation;
        this.ratingStar = ratingStar;

    }

    public int getIdRating() {
        return IdRating;
    }

    public void setIdRating(int idRating) {
        IdRating = idRating;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public int getIdRoomReservation() {
        return IdRoomReservation;
    }

    public void setIdRoomReservation(int idRoomReservation) {
        IdRoomReservation = idRoomReservation;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
