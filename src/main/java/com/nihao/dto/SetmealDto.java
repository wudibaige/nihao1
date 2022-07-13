package com.nihao.dto;

import com.nihao.entity.Setmeal;
import com.nihao.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
