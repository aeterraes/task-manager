package aeterraes.app.controller.dto;

import lombok.Data;

@Data
public class TaskDTO {
    String title;
    String description;
    int userId;
    private String status;
}
