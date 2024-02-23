package baykov.daniel.oracle11.service.util;

import baykov.daniel.oracle11.payload.GenericResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceUtil {

    public <T> GenericResponse<T> createGenericResponse(Page<?> content, List<T> mappedContent) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setContent(mappedContent);
        response.setPageNo(content.getNumber());
        response.setPageSize(content.getSize());
        response.setTotalElements(content.getTotalElements());
        response.setTotalPages(content.getTotalPages());
        response.setLast(content.isLast());
        return response;
    }
}
