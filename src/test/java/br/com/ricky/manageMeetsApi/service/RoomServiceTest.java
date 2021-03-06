package br.com.ricky.manageMeetsApi.service;

import br.com.ricky.manageMeetsApi.builder.RoomDTOBuilder;
import br.com.ricky.manageMeetsApi.dto.RoomDTO;
import br.com.ricky.manageMeetsApi.exception.ResourceNotFoundException;
import br.com.ricky.manageMeetsApi.mapper.RoomMapper;
import br.com.ricky.manageMeetsApi.model.Room;
import br.com.ricky.manageMeetsApi.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    private static final long INVALID_ROOM_ID = 1L;

    @Mock
    private RoomRepository roomRepository;

    private RoomMapper roomMapper = RoomMapper.INSTANCE;

    @InjectMocks
    private RoomService roomService;

    @Test
    void whenRoomInformedThenItShouldBeCreated() {
        // Given
        RoomDTO expectedRoomDTO = RoomDTOBuilder.builder().build().toRoomDTO();
        Room expectedSavedRoom = roomMapper.toModel(expectedRoomDTO);

        // When
        Mockito.when(roomRepository.save(expectedSavedRoom)).thenReturn(expectedSavedRoom);

        // Then
        RoomDTO createdRoomDTO = roomService.create(expectedRoomDTO);

        assertThat(createdRoomDTO.getId(), is(equalTo(expectedRoomDTO.getId())));
        assertThat(createdRoomDTO.getName(), is(equalTo(expectedRoomDTO.getName())));
        assertThat(createdRoomDTO.getDate(), is(equalTo(expectedRoomDTO.getDate())));
    }

    @Test
    void whenNotFoundThenAnExceptionShouldBeThrown() {
        // Given
        RoomDTO expectedRoomDTO = RoomDTOBuilder.builder().build().toRoomDTO();

        // When
        Mockito.when(roomRepository.findById(expectedRoomDTO.getId())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> roomService.getById(expectedRoomDTO.getId()));
    }

    @Test
    void whenValidRoomIdIsGivenThenReturnARoom() throws ResourceNotFoundException {
        // Given
        RoomDTO expectedRoomDTO = RoomDTOBuilder.builder().build().toRoomDTO();
        Room expectedFoundedRoom = roomMapper.toModel(expectedRoomDTO);

        // When
        Mockito.when(roomRepository.findById(expectedRoomDTO.getId())).thenReturn(Optional.of(expectedFoundedRoom));

        // Then
        RoomDTO foundRoomDTO = roomService.getById(expectedRoomDTO.getId());

        assertThat(foundRoomDTO, is(equalTo(expectedRoomDTO)));
    }

}
