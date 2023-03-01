package com.commbti.domain.bulletinboard.service;

import com.commbti.domain.bulletinboard.dto.BulletinPatchDto;
import com.commbti.domain.bulletinboard.dto.BulletinPostDto;
import com.commbti.domain.bulletinboard.dto.BulletinResponseDto;
import com.commbti.domain.bulletinboard.entity.Bulletin;
import com.commbti.domain.bulletinboard.repository.BulletinBoardRepository;
import com.commbti.domain.file.service.FileService;
import com.commbti.domain.member.entity.MbtiType;
import com.commbti.domain.member.entity.Member;
import com.commbti.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class BulletinServiceTest {

    @InjectMocks
    private BulletinService bulletinService;

    @Mock
    private BulletinBoardRepository bulletinBoardRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private FileService fileService;

    @Nested
    @DisplayName("게시글 등록")
    class CreateBulletinTest {
        @Test
        @DisplayName("게시글이 정상적으로 등록되는지 확인")
        void createBulletinTest() {
            Long memberId = 1L;
            Member member = new Member();
            String title = "test";
            String content = "test";
            MockMultipartFile multipartFile = new MockMultipartFile("테스트", "text.jpg", "image/jpg", "test".getBytes());
            String filePath = "/filePath";
            BulletinPostDto bulletinPostDto = new BulletinPostDto(title, content, multipartFile);

            given(memberService.findMember(memberId)).willReturn(member);
            given(fileService.upload(multipartFile)).willReturn(filePath);
            willDoNothing().given(bulletinBoardRepository).save(any());

            bulletinService.createBulletin(memberId, bulletinPostDto);
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class UpdateBulletinTest {
        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 제목, 내용, 이미지")
        void 모든_내용_수정_테스트() {
            Long memberId = 1L;
            Long bulletinId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Bulletin findBulletin = Bulletin.createArticle(title, content, filePath, member);

            String newTitle = "수정제목";
            String newContent = "수정내용";
            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BulletinPatchDto bulletinPatchDto = new BulletinPatchDto(newTitle, newContent, newMultipartFile);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(findBulletin));
            given(fileService.update(findBulletin.getFilePath(), bulletinPatchDto.getFile())).willReturn(newFilePath);

            bulletinService.updateBulletin(memberId, bulletinId, bulletinPatchDto);

            assertThat(findBulletin.getTitle()).isEqualTo(newTitle);
            assertThat(findBulletin.getContent()).isEqualTo(newContent);
            assertThat(findBulletin.getFilePath()).isEqualTo(newFilePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 제목")
        void 제목_수정_테스트() {
            Long memberId = 1L;
            Long bulletin = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Bulletin findBulletin = Bulletin.createArticle(title, content, filePath, member);

            String newTitle = "수정제목";
            BulletinPatchDto bulletinPatchDto = new BulletinPatchDto(newTitle, null, null);

            given(bulletinBoardRepository.findById(bulletin)).willReturn(Optional.of(findBulletin));
            given(fileService.update(findBulletin.getFilePath(), bulletinPatchDto.getFile())).willReturn(null);

            bulletinService.updateBulletin(memberId, bulletin, bulletinPatchDto);

            assertThat(findBulletin.getTitle()).isEqualTo(newTitle);
            assertThat(findBulletin.getContent()).isEqualTo(content);
            assertThat(findBulletin.getFilePath()).isEqualTo(filePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 내용")
        void 내용_수정_테스트() {
            Long memberId = 1L;
            Long bulletinId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Bulletin findBulletin = Bulletin.createArticle(title, content, filePath, member);

            String newContent = "수정내용";
            BulletinPatchDto bulletinPatchDto = new BulletinPatchDto(null, newContent, null);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(findBulletin));
            given(fileService.update(findBulletin.getFilePath(), bulletinPatchDto.getFile())).willReturn(null);

            bulletinService.updateBulletin(memberId, bulletinId, bulletinPatchDto);

            assertThat(findBulletin.getTitle()).isEqualTo(title);
            assertThat(findBulletin.getContent()).isEqualTo(newContent);
            assertThat(findBulletin.getFilePath()).isEqualTo(filePath);
        }

        @Test
        @DisplayName("게시글이 정상적으로 수정되는지 확인 - 이미지")
        void 이미지_수정_테스트() {
            Long memberId = 1L;
            Long bulletinId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Bulletin findBulletin = Bulletin.createArticle(title, content, filePath, member);

            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BulletinPatchDto bulletinPatchDto = new BulletinPatchDto(null, null, newMultipartFile);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(findBulletin));
            given(fileService.update(findBulletin.getFilePath(), bulletinPatchDto.getFile())).willReturn(newFilePath);

            bulletinService.updateBulletin(memberId, bulletinId, bulletinPatchDto);

            assertThat(findBulletin.getTitle()).isEqualTo(title);
            assertThat(findBulletin.getContent()).isEqualTo(content);
            assertThat(findBulletin.getFilePath()).isEqualTo(newFilePath);
        }

        @Test
        @DisplayName("권한이 없는 유저가 수정할 때 예외가 발생하는지 확인")
        void 권한없는_유저_수정_테스트() {
            Long memberId = 1L;
            Long fakeMemberId = 100L;
            Long bulletinId = 1L;

            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            String title = "기존제목";
            String content = "기존내용";
            String filePath = "/filePath";
            Bulletin findBulletin = Bulletin.createArticle(title, content, filePath, member);

            String newTitle = "수정제목";
            String newContent = "수정내용";
            MockMultipartFile newMultipartFile = new MockMultipartFile("수정이미지", "new.jpg", "image/jpg", "test".getBytes());
            String newFilePath = "/filePath";
            BulletinPatchDto bulletinPatchDto = new BulletinPatchDto(newTitle, newContent, newMultipartFile);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(findBulletin));

            Assertions.assertThatThrownBy(
                            () -> bulletinService.updateBulletin(fakeMemberId, bulletinId, bulletinPatchDto))
                    .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("잘못된 접근입니다.");
        }

        @Test
        @DisplayName("존재하지 않는 게시글을 수정 요청하였을 때 예외가 발생하는지 확인")
        void 존재하지_않는_게시글_수정_테스트() {
            Long fakeBulletinId = 100L;
            Long fakeMemberId = 100L;

            BulletinPatchDto fakeBulletinPatchDto = new BulletinPatchDto(null, null, null);
            given(bulletinBoardRepository.findById(fakeBulletinId)).willReturn(Optional.ofNullable(null));

            Assertions.assertThatThrownBy(
                            () -> bulletinService.updateBulletin(fakeMemberId, fakeBulletinId, fakeBulletinPatchDto))
                    .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("해당 게시글이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("게시물 조회 테스트")
    class FindBulletinTest {

        @DisplayName("게시글을 조회하였을 때 제대로 조회되는지 확인")
        @Test
        void 게시물_조회_테스트() {
            Long memberId = 1L;
            String nickname = "사용자A";
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);
            ReflectionTestUtils.setField(member, "nickname", nickname);
            member.updateMbtiType(MbtiType.INTJ);

            Long bulletinId = 1L;
            Bulletin savedBulletin = Bulletin.createArticle("테스트제목", "테스트내용", "/test.jpg", member);
            LocalDateTime createdAt = LocalDateTime.now();
            ReflectionTestUtils.setField(savedBulletin, "createdAt", createdAt);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(savedBulletin));

            BulletinResponseDto result = bulletinService.findOne(bulletinId);

            assertThat(result.getTitle()).isEqualTo(savedBulletin.getTitle());
            assertThat(result.getContent()).isEqualTo(savedBulletin.getContent());
            assertThat(result.getFilePath()).isEqualTo(savedBulletin.getFilePath());
            assertThat(result.getNickname()).isEqualTo(savedBulletin.getMember().getNickname());
            assertThat(result.getMbtiType()).isEqualTo(savedBulletin.getMember().getMbtiType());
            assertThat(result.getCreatedAt()).isEqualTo(savedBulletin.getCreatedAt());
        }

        @DisplayName("존재하지 않는 게시글을 조회하였을 때 예외가 발생하는지 확인")
        @Test
        void 존재하지_않는_게시물_조회_테스트() {
            Long bulletinId = 1L;

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.ofNullable(null));


            assertThatThrownBy(() -> {
                bulletinService.findOne(bulletinId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("존재하지 않는 게시글입니다.");
        }
    }

    @Nested
    @DisplayName("게시물 삭제 테스트")
    class DeleteBulletinTest {

        @Test
        @DisplayName("게시물이 정상적으로 삭제되는지 확인")
        void 게시물_삭제_테스트() {
            Long memberId = 1L;
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            Long bulletinId = 1L;
            Bulletin savedBulletin = Bulletin.createArticle("테스트제목", "테스트내용", "/test.jpg", member);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(savedBulletin));
            willDoNothing().given(bulletinBoardRepository).delete(savedBulletin);

            boolean result = bulletinService.deleteOne(memberId, bulletinId);

            assertThat(result).isTrue();
        }
        @Test
        @DisplayName("존재하지 않는 게시물을 삭제 요청하였을 때 예외가 발생하는지 확인")
        void 존재하지_않는_게시물_삭제_테스트() {
            Long memberId = 1L;
            Long fakeBulletinId = 100L;
            given(bulletinBoardRepository.findById(fakeBulletinId)).willReturn(Optional.ofNullable(null));

            assertThatThrownBy(() -> {
                bulletinService.deleteOne(memberId, fakeBulletinId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("존재하지 않는 게시글입니다.");
        }
        @Test
        @DisplayName("권한없는 유저가 삭제 요청하였을 때 예외가 발생하는지 확인")
        void 권한없는_유저_게시물_삭제_테스트() {
            Long memberId = 1L;
            Long fakeMemberId = 100L;
            Member member = new Member();
            ReflectionTestUtils.setField(member, "id", memberId);

            Long bulletinId = 1L;
            Bulletin savedBulletin = Bulletin.createArticle("테스트제목", "테스트내용", "/test.jpg", member);

            given(bulletinBoardRepository.findById(bulletinId)).willReturn(Optional.of(savedBulletin));

            assertThatThrownBy(() -> {
                bulletinService.deleteOne(fakeMemberId, bulletinId);
            }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("잘못된 접근입니다.");
        }
    }
}
