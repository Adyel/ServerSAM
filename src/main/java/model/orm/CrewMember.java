package model.orm;

import com.github.dozermapper.core.Mapping;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;



@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class CrewMember extends BaseMember {

  @Column(name = "department")
  @Mapping("department")
  public String department;

  @Column(name = "job")
  @Mapping("job")
  public String job;
}
