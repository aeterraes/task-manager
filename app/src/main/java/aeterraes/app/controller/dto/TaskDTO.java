package aeterraes.app.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskDTO implements Serializable {
    String title;
    String description;
    int userId;
}
