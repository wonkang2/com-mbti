/**
 * 파일 삭제시 해당 파일 id를 담을 배열
 */
const deleteFileIdList = [];

/**
 * 첨부파일 삭제 관련
 */
let deleteButtons = document.getElementsByClassName('article__files__deleteButton');
let fileDeleteButtons = document.querySelectorAll('.article__files__deleteButton');
fileDeleteButtons.forEach(function (button) {
    console.log("deleteButtons", deleteButtons);
    console.log("fileDeleteButtons", fileDeleteButtons);
    button.addEventListener('click', function (event) {
        console.log("첨부파일 삭제 버튼 클릭 이벤트");
        if (confirm("정말로 삭제하시겠습니까?")) {
            const target = event.target;
            const deleteImageId = target.getAttribute('data-deleteImage-id');
            console.log("deleteImageId: ", deleteImageId);

            deleteFileIdList.push(deleteImageId);
            console.log("deleteFileIdList: ", deleteFileIdList);
            const imageWrapper = document.getElementById('image' + deleteImageId);
            imageWrapper.remove();
            alert("삭제되었습니다.");
        }
    });
})

/**
 * 게시글 저장 관련
 */
const saveButton = document.getElementById('saveButton');
saveButton.addEventListener('click', function () {
    const bulletinId = saveButton.getAttribute("data-bulletinId");
    const title = document.getElementById("title").value;
    const content = document.getElementById("content").value;
    const files = document.getElementById("files").files;

    console.log("title: ", title);
    console.log("content: ", content);
    console.log("files: ", files);

    if (!validate(title, content)) {
        return false;
    }

    const formData = new FormData();
    formData.append("title", title);
    formData.append("content", content);
    for (let i = 0; i < files.length; i++) {
        formData.append("files[" + i + "]", files[i]);
    }
    for (let i = 0; i < deleteFileIdList.length; i++) {
        formData.append("deleteFiles[" + i + "]", deleteFileIdList[i]);
    }
    for (let key of formData.keys()) {
        console.log(key, ":", formData.get(key));
    }

    fetch('/api/bulletins/' + bulletinId, {
        method: 'PATCH',
        body: formData
    }).then(response => {
        if (response.ok) {
            alert("수정되었습니다.");
            window.location.replace(response.headers.get("Location"));
        } else {
            console.log("HTTP Error!!!");
            throw new Error('다시 요청해주세요.');
        }
    }).catch(error => alert(error));
});

/**
 * 입력값을 체크하는 메서드
 * @param title
 * @param content
 * @returns {boolean}
 */
function validate(title, content) {
    if (title == "") {
        alert("제목은 필수입니다.");
        title.focus();
        return false;
    }

    if (content == "") {
        alert("내용은 필수입니다.");
        content.focus();
        return false;
    }
    return true;
}
