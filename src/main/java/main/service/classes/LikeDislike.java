package main.service.classes;

import lombok.Getter;

@Getter

public class LikeDislike {
  private int like;
  private int dislike;

  public void addLike() {
    like++;
  }

  public void addDislike() {
    dislike++;
  }
}
