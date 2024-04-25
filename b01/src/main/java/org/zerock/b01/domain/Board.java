package org.zerock.b01.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
@ToString(exclude = "imageSet")
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

    /*
    * Board             -            BoardImae
    * 부모               -              자식
    * One(1)            -            Many(다)
    *
    * 아래 설정을 하면 @OneToMany 의 불필요한 복합 연결테이블이 생성되지 않는다.
    * mappedBy = "board"는
    * BoardImage의 private Board board; 필드를 Fk로 지정한 것
    *
    * CascadeType.All
    * 게시판의부모 글이 삭제 되면, 소속된 자식 이미지도 삭제되도록 한다.
    * */
    @OneToMany(mappedBy = "board",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages() {

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }
}
