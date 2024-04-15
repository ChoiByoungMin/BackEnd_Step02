package org.zerock.springex.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

    /* getTime()메서드가 실행될 때 select now() 쿼리가 실행되고
    결과값인 시간을 문자열로 반환한다.
    * */
    @Select("select now()")
    String getTime();
}
