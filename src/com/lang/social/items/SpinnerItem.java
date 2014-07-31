package com.lang.social.items;

public class SpinnerItem {

    int drawableResID;
    String name;

    public SpinnerItem(int drawableResID, String name) {
          super();
          this.drawableResID = drawableResID;
          this.name = name;
    }

    public int getDrawableResID() {
          return drawableResID;
    }
    public void setDrawableResID(int drawableResID) {
          this.drawableResID = drawableResID;
    }
    public String getName() {
          return name;
    }
    public void setName(String name) {
          this.name = name;
    }
}
