package antidimon.web.notificationservice.mappers;

import antidimon.web.notificationservice.models.Notification;
import antidimon.web.notificationservice.models.dto.NotificationOutputDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public NotificationOutputDTO toDTO(Notification notification) {
        return modelMapper.map(notification, NotificationOutputDTO.class);
    }
}
