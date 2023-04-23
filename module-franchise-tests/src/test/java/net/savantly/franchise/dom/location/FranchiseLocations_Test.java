package net.savantly.franchise.dom.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

@ExtendWith(MockitoExtension.class)
class FranchiseLocations_Test {

    @Mock RepositoryService mockRepositoryService;
    @Mock JpaSupportService mockJpaSupportService;
    @Mock FranchiseLocationRepository mockFranchiseLocationRepository;

    FranchiseLocations objects;

    @BeforeEach
    public void setUp() {
        objects = new FranchiseLocations(mockRepositoryService, mockJpaSupportService, mockFranchiseLocationRepository);
    }

    @Nested
    class create {

        @Test
        void happyCase() {

            // given
            final String someName = "Foobar";

            // expect
            when(mockRepositoryService.persist(
                    argThat((ArgumentMatcher<FranchiseLocation>) simpleObject -> Objects.equals(simpleObject.getName(), someName)))
            ).then((Answer<FranchiseLocation>) invocation -> invocation.getArgument(0));

            // when
            final FranchiseLocation obj = objects.create(someName);

            // then
            assertThat(obj).isNotNull();
            assertThat(obj.getName()).isEqualTo(someName);
        }
    }

    @Nested
    class ListAll {

        @Test
        void happyCase() {

            // given
            final List<FranchiseLocation> all = new ArrayList<>();

            // expecting
            when(mockFranchiseLocationRepository.findAll())
                .thenReturn(all);

            // when
            final List<FranchiseLocation> list = objects.listAll();

            // then
            assertThat(list).isEqualTo(all);
        }
    }
}
