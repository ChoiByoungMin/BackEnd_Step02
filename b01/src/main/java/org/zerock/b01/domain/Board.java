package org.zerock.b01.domain;

import lombok.*;

import javax.persistence.*;

/* DB 논리적 설계단계에서 물리적 설계로 전환되기 전에
물리적 Table로 생성되어야 할 논리적 묶음을 Entity라고 한다.

그래서 종종 Entity와 Table을 동일한 개념으로 사용하곤 한다.
* */

// 이 클래스의 정보를 가지고 자동으로 Board Table 생성할거야
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity {

    /* Id는 Pk(primary Key)로 정의한다는 뜻
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    는 MySql/MariaDB 에서 auto_increment 속성 부여
    * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @Column(length = 500, nullable = false) //컬럼의 길이와 null허용여부
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    public void change(String title, String content){
        this.title = title;
        this.content = content;
    }
}
