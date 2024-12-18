document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('evaluateForm');
    const submitButton = document.getElementById('submitEvaluateBtn');

    submitButton.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent default form submission

        const productID = form.querySelector('input[name="productID"]').value;
        const evaluateContent = form.querySelector('textarea[name="evaluateContent"]').value;
        
        const formData = new FormData(form);
        

         // Create the request body as FormData
        const payload = {
            productID: productID,
            evaluateContent: evaluateContent
        }
      
        fetch('/product_customer/evaluate', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text(); // Or response.json() if your server returns JSON
        })
        .then(data => {
           console.log('Success', data);
            // Handle success response from server (e.g., display message, redirect, etc.)
             // Example: Show an alert message
            alert('Đánh giá của bạn đã được gửi thành công!');
        })
        .catch(error => {
             console.error('Fetch Error:', error);
            // Handle errors (e.g., display error message to user)
             alert('Vui lòng nhập đánh giá trước khi gửi!');
        });
    });





    const reviewsTab = document.getElementById('reviews-tab');
    const reviewContainer = document.getElementById('reviews');

    if (reviewsTab) {
         reviewsTab.addEventListener('show.bs.tab', function(event) {
              
                   const productID = document.querySelector('input[name="productID"]').value;
                    fetch(`/product_customer/getEvaluates?productID=${productID}`, {
                         method: 'GET',
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${response.status}`);
                         }
                        return response.json();
                        
                    })
                    .then(evaluates => {
                         if(evaluates.length === 0){
                            const noReviewsMessage = document.createElement('p');
                            noReviewsMessage.textContent = 'Chưa có đánh giá nào cho sản phẩm này.';
                            reviewContainer.appendChild(noReviewsMessage);
                            return;
                         }
                        console.log('Success', evaluates);
                        evaluates.forEach(userEvaluate => {
                             const reviewParagraph = document.createElement('p');
                                reviewParagraph.style.color = '#7c4dff'; // Apply the color
                                const spanSeparator = document.createElement('span');
                                spanSeparator.textContent = ' --- ';
                                const spanContent = document.createElement('span');
                                spanContent.textContent = userEvaluate.evaluateContent;
                                reviewParagraph.appendChild(spanSeparator)
                                reviewParagraph.appendChild(spanContent)
                             reviewContainer.appendChild(reviewParagraph);
                        });
                    })
                   .catch(error => {
                       console.error('Fetch Error:', error);
                       const errorMessage = document.createElement('p');
                       errorMessage.textContent = 'Có lỗi xảy ra khi tải đánh giá.';
                       reviewContainer.appendChild(errorMessage);
                   });
            
         });
    }
});