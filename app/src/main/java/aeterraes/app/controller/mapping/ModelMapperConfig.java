package aeterraes.app.controller.mapping;

import aeterraes.app.controller.dto.TaskDTO;
import aeterraes.app.dataaccess.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(TaskDTO.class, Task.class).addMappings(mapper -> {
            mapper.skip(Task::setId);
        });
        return modelMapper;
    }
}
