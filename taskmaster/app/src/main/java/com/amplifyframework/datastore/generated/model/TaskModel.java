package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the TaskModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TaskModels", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byTeam", fields = {"teamId","name"})
public final class TaskModel implements Model {
  public static final QueryField ID = field("TaskModel", "id");
  public static final QueryField NAME = field("TaskModel", "name");
  public static final QueryField STATE = field("TaskModel", "state");
  public static final QueryField DESCRIPTION = field("TaskModel", "description");
  public static final QueryField TEAM = field("TaskModel", "teamId");
  public static final QueryField S3_IMAGE_KEY = field("TaskModel", "s3ImageKey");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="TaskStateEnum") TaskStateEnum state;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="Team") @BelongsTo(targetName = "teamId", targetNames = {"teamId"}, type = Team.class) Team team;
  private final @ModelField(targetType="String") String s3ImageKey;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public TaskStateEnum getState() {
      return state;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Team getTeam() {
      return team;
  }
  
  public String getS3ImageKey() {
      return s3ImageKey;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TaskModel(String id, String name, TaskStateEnum state, String description, Team team, String s3ImageKey) {
    this.id = id;
    this.name = name;
    this.state = state;
    this.description = description;
    this.team = team;
    this.s3ImageKey = s3ImageKey;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TaskModel taskModel = (TaskModel) obj;
      return ObjectsCompat.equals(getId(), taskModel.getId()) &&
              ObjectsCompat.equals(getName(), taskModel.getName()) &&
              ObjectsCompat.equals(getState(), taskModel.getState()) &&
              ObjectsCompat.equals(getDescription(), taskModel.getDescription()) &&
              ObjectsCompat.equals(getTeam(), taskModel.getTeam()) &&
              ObjectsCompat.equals(getS3ImageKey(), taskModel.getS3ImageKey()) &&
              ObjectsCompat.equals(getCreatedAt(), taskModel.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), taskModel.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getState())
      .append(getDescription())
      .append(getTeam())
      .append(getS3ImageKey())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TaskModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("state=" + String.valueOf(getState()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("team=" + String.valueOf(getTeam()) + ", ")
      .append("s3ImageKey=" + String.valueOf(getS3ImageKey()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static TaskModel justId(String id) {
    return new TaskModel(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      state,
      description,
      team,
      s3ImageKey);
  }
  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    TaskModel build();
    BuildStep id(String id);
    BuildStep state(TaskStateEnum state);
    BuildStep description(String description);
    BuildStep team(Team team);
    BuildStep s3ImageKey(String s3ImageKey);
  }
  

  public static class Builder implements NameStep, BuildStep {
    private String id;
    private String name;
    private TaskStateEnum state;
    private String description;
    private Team team;
    private String s3ImageKey;
    @Override
     public TaskModel build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TaskModel(
          id,
          name,
          state,
          description,
          team,
          s3ImageKey);
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep state(TaskStateEnum state) {
        this.state = state;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep team(Team team) {
        this.team = team;
        return this;
    }
    
    @Override
     public BuildStep s3ImageKey(String s3ImageKey) {
        this.s3ImageKey = s3ImageKey;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, TaskStateEnum state, String description, Team team, String s3ImageKey) {
      super.id(id);
      super.name(name)
        .state(state)
        .description(description)
        .team(team)
        .s3ImageKey(s3ImageKey);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder state(TaskStateEnum state) {
      return (CopyOfBuilder) super.state(state);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder team(Team team) {
      return (CopyOfBuilder) super.team(team);
    }
    
    @Override
     public CopyOfBuilder s3ImageKey(String s3ImageKey) {
      return (CopyOfBuilder) super.s3ImageKey(s3ImageKey);
    }
  }
  
}
