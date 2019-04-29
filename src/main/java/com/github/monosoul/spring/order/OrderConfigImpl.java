package com.github.monosoul.spring.order;

import java.util.List;
import lombok.Value;

@Value
public class OrderConfigImpl<T> implements OrderConfig<T> {

    List<? extends OrderConfigItem<? extends T>> items;
}
