package com.github.bc29ea3c101054baa1429ffc2edba4ae.planks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Record implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int rId;

    @ColumnInfo(name = "rDate")
    public Long rDate;

    @ColumnInfo(name = "rScore")
    public Long rScore;


    public int getrId(){ return rId; }
    public Long getrDate(){
        return rDate;
    }
    public Long getrScore() {return rScore; }


    public void setId(int rId){ this.rId = rId; }
    public void setDate(Long rDate){ this.rDate = rDate; }
    public void setScore(Long rScore){ this.rScore = rScore; }

}
