package net.savantly.nexus.franchise.dom.location;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.savantly.nexus.franchise.dom.location.FranchiseLocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

@ExtendWith(MockitoExtension.class)
class FranchiseLocation_Test {

    @Mock TitleService mockTitleService;
    @Mock MessageService mockMessageService;
    @Mock RepositoryService mockRepositoryService;

    FranchiseLocation object;

    @BeforeEach
    public void setUp() throws Exception {
        object = FranchiseLocation.withRequiredFields("Foo");
        object.titleService = mockTitleService;
        object.messageService = mockMessageService;
        object.repositoryService = mockRepositoryService;
    }

    @Nested
    public class updateName {

        @Test
        void happy_case() {
            // given
            assertThat(object.getName()).isEqualTo("Foo");

            // when
            object.updateName("Bar");

            // then
            assertThat(object.getName()).isEqualTo("Bar");
        }

    }
    @Nested
    class delete {

        @Test
        void happy_case() throws Exception {

            // given
            assertThat(object).isNotNull();

            // expecting
            when(mockTitleService.titleOf(object)).thenReturn("Foo");

            // when
            object.delete();

            // then
            verify(mockMessageService).informUser("'Foo' deleted");
            verify(mockRepositoryService).removeAndFlush(object);
        }
    }
}
