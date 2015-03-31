package com.prosidney.domain;

import android.content.Context;
import com.orm.SugarRecord;

/**
 * Created by Sidney on 15-03-31.
 */
public class Establishment extends SugarRecord<Establishment> {

    private String name;

    public Establishment() {
    }

    public Establishment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
