package me.dpidun.photobox.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    public enum ProcessingStatus {
        CREATED,
        PROCESSING_STEP_WATERMARK,
        PROCESSING_STEP_THUMBNAIL,
        PROCESSING_STEP_FINISHING,
        FINISHED,
        FAILED;
    }


}


