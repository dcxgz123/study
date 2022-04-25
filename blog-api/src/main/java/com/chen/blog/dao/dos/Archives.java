package com.chen.blog.dao.dos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;
}
