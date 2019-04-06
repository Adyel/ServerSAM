package model.orm;


import com.github.dozermapper.core.Mapping;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseMember {

  @Id
  @Column(name = "member_id")
  @Mapping("id")
  public Integer id;

  @Column(name = "credit_id")
  @Mapping("credit_id")
  public String creditId;

  @Column(name = "name")
  @Mapping("name")
  public String name;

  @Column(name = "profile_path")
  @Mapping("profile_path")
  public String profilePath;
}
