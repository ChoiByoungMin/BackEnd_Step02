package org.zerock.springex.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


/*
* 현재는 E에 TodoDto가 전달 된다.
*
* 하지만 다른 업무/ 목적으로 얼만든지 많은 테이블과 dto가 생성될 수 있다.
* 이 때 dtoList를 제외한 나머지 값들은 어떤 page에서도 pagination에 꼭 필요한 데이터이다.
* dtoList만 할일 페이지 업무일 때는 TodoDTO
*           예를 들어 Project 업무일 때는 ProjectDTO
*           쇼핑 구매 업무일 때는 orderDTO 등의 list가 필요하므로
*           geriric으로 만들면 1번 만들어놓고 재사용이 가능ㅎ하다.
* */
@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    //시작 페이지 번호
    private int start;
    //끝 페이지 번호
    private int end;

    //이전 페이지의 존재 여부
    private boolean prev;
    //다음 페이지의 존재 여부
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){
        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        // 내 page 번호에 따라 pagination의 끝번호를 구하고 -9를 해주면 시작번호를 구할 수 있다.
        // 10은 pagination의 범위이다.
        this.end = (int)(Math.ceil(this.page / 10.0 )) * 10;

        this.start = this.end - 9;

        // pagination의 가장 끝 번호
        int last = (int)(Math.ceil((total/(double)size)));

        /* 위에서 end계산은 10단위로 무조건 맞춘 것이기 때문에
        실제 마지막 페이지 번호와 확인 해서
        마지막 페이지인 경우는 end > last보다 크게 되므로
        end = last로 해줘야
        마지막 페이지인 경우 pagination이 알맞게 나오게 된다.
        * */
        this.end = end > last ? last : end;

        this.prev = this.start > 1;

        // 현재 pagination 이 보여주는 범위가 마지막이 아니므로 다음페이지가 존재
        this.next = total > this.end * this.size;
    }
}
