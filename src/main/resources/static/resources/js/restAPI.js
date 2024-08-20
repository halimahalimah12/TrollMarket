"use script"
const url = 'http://localhost:8081/api/v1';

(function () {
        $('.success-alert').hide();
        $('.alert-gagal').hide();
       insertShipment();

       submitShipment();
       updateShipment();
       showAlertSuccess();
       deleteShipment();
       submitDelete();
       stopService();
       submitStopService();
       closeModal();
       addBalanceBuyer();
       submitAddBalance();
       btnDiscontinue();
       submitDiscontinue();
       btnDeleteProduct();
       submitDeleteProduct();
       btnInfoProduct();
       btnInfoProductInShop();
       btnAddToCart();
       submitAddCart();
       dataMyCart();
       submitRemoveProductFromCart();
       inputFormatRupiah();
       fileInput();
}())

function fileInput(){
   document.getElementById('fileInput').addEventListener('change', function (event) {
       const image = document.getElementById('image');
       const file = event.target.files[0];
       const reader = new FileReader();

       reader.onload = function (e) {
           image.src = e.target.result;
           image.style.display = 'block';

           const cropper = new Cropper(image, {
               aspectRatio: 1,
               viewMode: 1,
               ready: function () {
                   const cropBoxData = cropper.getCropBoxData();
                   const cropBoxWidth = cropBoxData.width;
                   cropper.setCropBoxData({
                       left: (cropper.getContainerData().width - cropBoxWidth) / 2,
                       top: (cropper.getContainerData().height - cropBoxWidth) / 2,
                       width: cropBoxWidth,
                       height: cropBoxWidth
                   });
               }
           });

           document.getElementById('cropButton').addEventListener('click', function () {
               const canvas = cropper.getCroppedCanvas({
                   width: 300,
                   height: 300,
                   imageSmoothingEnabled: true,
                   imageSmoothingQuality: 'high'
               });

               document.getElementById('canvas').getContext('2d').drawImage(canvas, 0, 0);
           });
       };

       reader.readAsDataURL(file);
   });
}




function inputFormatRupiah(){
   var input = document.getElementById('price');
   input.addEventListener('keyup', function(e)
    {
       input.value = formatRupiah(this.value);
    });
}

//FORMAT RUPIAH
function formatRupiah(angka, prefix)
	{
		var number_string = angka.replace(/[^,\d]/g, '').toString(),
			split	= number_string.split(','),
			sisa 	= split[0].length % 3,
			rupiah 	= split[0].substr(0, sisa),
			ribuan 	= split[0].substr(sisa).match(/\d{3}/gi);

		if (ribuan) {
			separator = sisa ? '.' : '';
			rupiah += separator + ribuan.join('.');
		}

		rupiah = split[1] != undefined ? rupiah + ',' + split[1] : rupiah;
		return prefix == undefined ? rupiah : (rupiah ? 'Rp. ' + rupiah : '');
	}


//SHIPMENT
function insertShipment(){
     $('#btnAddShipment').click(function(event){
        $('#checkBoxService').show();
     })
}
function closeModal(){
    $('.close').click(function(event){
        location.reload();
    })
}
function submitShipment(){
    $('.formUpsert .submit').click(function(event){
        event.preventDefault();
         let dto = collectInputForm();
         console.log(dto);
         let requestMethod = (dto.id === null) ? 'POST' : 'PUT';
         let requestUrl = (dto.id === null) ? `${url}/shipment` : `${url}/shipment/${dto.id}`;
              let token = localStorage.getItem('token');
           $.ajax({
                     method: requestMethod,
                     url: requestUrl,
                     headers:{"Authorization": `Bearer ${token}`
                             },
                     data: JSON.stringify(dto),
                     contentType: 'application/json',
                     success: function(response){
                        localStorage.setItem('successMessage', "Successfully Added/Update Shipment");
                         location.reload();
                     },
                     error: function({status, responseJSON}){
                         if(status === 406){
                             console.log(responseJSON);
                         } else if(status == 409){
                            alert(responseJSON);
                         }else{
                            alert(`Error: ${status}`);
                         }
                     }
           });

    })

}
function showAlertSuccess(){
    let successMessage = localStorage.getItem('successMessage');
    if(successMessage){
        $('.success-alert').show().text(successMessage);
    }
    localStorage.removeItem('successMessage');
}
function collectInputForm(){
    let id = $('.formUpsert #id').val();
    let dto = {
        id: (id === "") ? null : id,
        name: $('.formUpsert #name').val(),
        price: $('.formUpsert #price').val().replaceAll('.',''),
        isService: $('.formUpsert #isService').prop('checked')
    }
    return dto;
}
function updateShipment(){
   $(document).on('click', '.btnEdit', function(event){
         event.preventDefault();
         $('#checkBoxService').hide();
         let shipmentId = $(this).attr('data-id');
          $.ajax({
              method:'GET',
              url: `${url}/shipment/${shipmentId}`,
              headers:{"Content-Type" : "application/json",
                       "Authorization": `Bearer ${localStorage.getItem('token')}`
                      },
             success: function(response){
                 populateInputForm(response);
             }
         })


    })

}
function populateInputForm({id, name, price}){
    $('#id').val(id);
    $('#name').val(name);
    $('#price').val(price);
}
function deleteShipment(){
     $(document).on('click', '.btn-delete', function(event){
         event.preventDefault();
        let shipmentId = $(this).attr('data-id');
        $('.modal-content .id').val(shipmentId);
     })
}
function submitDelete(){
 $(document).off('click', '.modal-content .remove');
    $(document).on('click', '.modal-content .remove',function (event){
        let shipmentId = $('.modal-content .id').val();
        event.preventDefault();
        $.ajax({
            method: "DELETE",
            url: `${url}/shipment/${shipmentId}`,
             headers:{"Content-Type" : "application/json",
                      "Authorization": `Bearer ${localStorage.getItem('token')}`
             },
            success: function (response) {
                localStorage.setItem('successMessage', "Successfully Delete Shipment")
                location.reload();
            },
            error: function (response) {
                alert(`Ada error dengan status code: ${response.status}`);
            }
        });
    })
}
function stopService(){
     $(document).on('click', '.btn-stopService', function(event){
             event.preventDefault();
            let shipmentId = $(this).attr('data-id');
            $('.modal-content .id').val(shipmentId);
         })
}
function submitStopService(){
    $(document).off('click', '.modal-content .stopService');
        $(document).on('click', '.modal-content .stopService',function (event){
            let shipmentId = $('.modal-content .id').val();
            event.preventDefault();
            $.ajax({
                method: "PUT",
                url: `${url}/shipment/stopService/${shipmentId}`,
                 headers:{"Content-Type" : "application/json",
                          "Authorization": `Bearer ${localStorage.getItem('token')}`
                 },
                success: function (response) {
                    localStorage.setItem('successMessage', "Successfully Stop Service Shipment")
                    location.reload();
                },
                error: function (response) {
                    alert(`Ada error dengan status code: ${response.status}`);
                }
            });
        })
}

//BUYER
function addBalanceBuyer(){
    $(document).on('click', '.btn-balance', function(event){
             event.preventDefault();
            let buyerId = $(this).attr('data-id');
            $('.id').val(buyerId);
        })
}
function submitAddBalance(){
      $('.formAddBalance .addBalance').click(function(event){
       event.preventDefault();
                let dto = collectInputFormBalance();
                console.log(dto);
                $.ajax({
                    method: "PUT",
                    data: JSON.stringify(dto),
                    url: `${url}/profile/${dto.id}`,
                    headers:{"Content-Type" : "application/json",
                              "Authorization": `Bearer ${localStorage.getItem('token')}`
                    },
                    success: function (response) {
                        localStorage.setItem('successMessage', "Successfully Add Balance")
                        location.reload();
                    },
                    error: function (response) {
                        alert(`Ada error dengan status code: ${response.status}`);
                    }
                });
       })
}
function collectInputFormBalance(){
    let dto = {
        id: $('.id').val(),
        balance: $('#balance').val()
    }
    return dto;
}

//PRODUCT
function btnDiscontinue(){
     $(document).on('click', '.btnDiscontinue', function(event){
             event.preventDefault();
            let productId = $(this).attr('data-id');
            $('.modal-content .id').val(productId);
         })
}
function submitDiscontinue(){
    $(document).off('click', '.modal-content .discontinue');
        $(document).on('click', '.modal-content .discontinue',function (event){
            let productId = $('.modal-content .id').val();
            event.preventDefault();
            $.ajax({
                method: "PUT",
                url: `${url}/merchandise/discontinue/${productId}`,
                 headers:{"Content-Type" : "application/json",
                          "Authorization": `Bearer ${localStorage.getItem('token')}`
                 },
                success: function (response) {
                    localStorage.setItem('successMessage', "Successfully Discontinue Merchandise")
                    location.reload();
                },
                error: function (response) {
                    alert(`Ada error dengan status code: ${response.status}`);
                }
            });
        })
}
function btnDeleteProduct(){
     $(document).on('click', '.btnDelete', function(event){
             event.preventDefault();
            let productId = $(this).attr('data-id');
            $('.modal-content .id').val(productId);
         })
}
function submitDeleteProduct(){
    $(document).off('click', '.modal-content .delete');
        $(document).on('click', '.modal-content .delete',function (event){
            let productId = $('.modal-content .id').val();
            event.preventDefault();
            $.ajax({
                method: "DELETE",
                url: `${url}/merchandise/${productId}`,
                 headers:{"Content-Type" : "application/json",
                          "Authorization": `Bearer ${localStorage.getItem('token')}`
                 },
                success: function (response) {
                    localStorage.setItem('successMessage', "Successfully Discontinue Merchandise")
                    location.reload();
                },
                error: function (response) {
                    alert(`Ada error dengan status code: ${response.status}`);
                }
            });
        })
}
function btnInfoProduct(){
      $(document).on('click', '.btnInfo', function(event){
         event.preventDefault();
        let productId = $(this).attr('data-id');
         $.ajax({
            method: "GET",
            url: `${url}/merchandise/${productId}`,
             headers:{
                      "Authorization": `Bearer ${localStorage.getItem('token')}`
             },
            success: function (response) {
                console.log(response);
                infoProduct(response);
                $('#info').modal('show')
            },
            error: function (response) {
                alert(`Ada error dengan status code: ${response.status}`);
            }
        });

    })
}
function infoProduct(response){
    let tbody = $('.infoProduct .dataProduct');
    let temp =
        `
            <tr>
             <th scope="row">Name</th>
              <td>${response.name}</td>
            </tr>
            <tr>
              <th scope="row">Category</th>
              <td>${response.category}</td>
            </tr>
            <tr>
              <th scope="row">Description</th>
              <td>${response.description}</td>
            </tr>
            <tr>
              <th scope="row">Price</th>
              <td>${response.price}</td>
            </tr>
            <tr>
              <th scope="row">Discontinue</th>
              <td>${response.discontinue}</td>
            </tr>
        `

    tbody.html(temp);

}

//SHOP
function btnInfoProductInShop(){
     $(document).on('click', '.btnInfoInShop', function(event){
             event.preventDefault();
            let productId = $(this).attr('data-id');
             $.ajax({
                method: "GET",
                url: `${url}/merchandise/${productId}`,
                 headers:{
                          "Authorization": `Bearer ${localStorage.getItem('token')}`
                 },
                success: function (response) {
                    console.log(response);
                    infoProductInShop(response);
                    $('#infoProductInShop').modal('show')
                },
                error: function (response) {
                    alert(`Ada error dengan status code: ${response.status}`);
                }
            });

        })

}
function infoProductInShop(response){
    let tbody = $('.infoProduct .dataProduct');
        let temp =
            `
                <tr>
                 <th scope="row">Name</th>
                  <td>${response.name}</td>
                </tr>
                <tr>
                  <th scope="row">Category</th>
                  <td>${response.category}</td>
                </tr>
                <tr>
                  <th scope="row">Description</th>
                  <td>${response.description}</td>
                </tr>
                <tr>
                  <th scope="row">Price</th>
                  <td>${response.price}</td>
                </tr>
                <tr>
                  <th scope="row">Seller Name</th>
                  <td>${response.seller}</td>
                </tr>
            `

        tbody.html(temp);
}
function btnAddToCart(){
      $(document).on('click', '.addCart', function(event){
         event.preventDefault();
          let productId = $(this).attr('data-id');
          let buyerId = $(this).attr('data-buyerId');
          $('.modal-content #idProduct').val(productId);
          $('.modal-content .buyerId').val(buyerId);
         $.ajax({
             method: "GET",
             url: `${url}/shipment`,
              headers:{
                       "Authorization": `Bearer ${localStorage.getItem('token')}`
              },
             success: function (response) {
                 console.log(response);
                 dropDownShipment(response);
                 $('#addToCart').modal('show')
             },
             error: function (response) {
                 alert(`Ada error dengan status code: ${response.status}`);
             }
         });

      });

}
function dropDownShipment(data) {
    const select = document.getElementById('shipmentDropdown');
    select.innerHTML = '';

    const defaultOption = document.createElement('option');
    defaultOption.text = 'Pilih opsi';
    defaultOption.value = '';
    select.add(defaultOption);

    // Tambahkan opsi dari data
    data.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.text = item.name;
        select.add(option);
    });
}
function populateInputFormAddCart(){
    let dto = {
        buyerId: $('.buyerId').val(),
        productId: $('#idProduct').val(),
        quantity: $('#quantity').val(),
        shipmentId: $('#shipmentDropdown').val()
    }
    return dto;
}
function submitAddCart(){
     $(document).on('click', '.modal-content .submitCart',function (event){
        event.preventDefault();
        let dto = populateInputFormAddCart();

        $.ajax({
            method: "POST",
            url: `${url}/cart`,
            headers:{"Authorization": `Bearer ${localStorage.getItem('token')}`
            },
            data: JSON.stringify(dto),
            contentType: 'application/json',
            success: function (response) {
                if(response.isSuccess == true){
                    localStorage.setItem('successMessage', response.pesan);
                    location.reload();
                }else{
                    $('.alert-gagal').show().text(response.pesan);
                }

            },
            error: function (response) {
                alert(`Ada error dengan status code: ${response.status}`);
            }
        });
    })

}

//My Cart
function dataMyCart(){
    let buyerId = $('.buyerId').val();
    $.ajax({
        type:"GET",
        headers:{
                 "Authorization": `Bearer ${localStorage.getItem('token')}`
        },
        url:`${url}/cart/${buyerId}`,
        success: function(result){
            setTable(result);
            console.log(result);
        }

    })

}

function setTable(data){
       const tbody = document.querySelector("tbody");
        let temp = "";
        data.forEach((cart) => {
            temp = temp +
                `
                <tr id ="record">
                    <td>
                        <a href="javascript:;" class="btn btn-outline-primary remove-button"  data-productId="${cart.cartId.productId}"
                        data-shipmentId="${cart.cartId.shipmentId}" data-buyerId="${cart.cartId.buyerId}"
                        >Remove</a>
                    </td>
                        <td>${cart.nameProduct}</td>
                        <td>${cart.quantity}</td>
                        <td>${cart.shipment}</td>
                        <td>${cart.seller}</td>
                        <td>${cart.totalPrice}</td>
                </tr>
                `
        });

        tbody.innerHTML = temp;
        showModalRemove();

}

function showModalRemove(){
    $('.remove-button').click(function(event){
           let productId= $(this).attr('data-productId');
           $('#productId').val(productId);
           let shipmentId =$(this).attr('data-shipmentId');
           $('#shipmentId').val(shipmentId);
           let buyerId =$(this).attr('data-buyerId');
           $('#buyerId').val(buyerId);


           // Menampilkan modal "removeProductFromCart"
           $('#removeProductFromCart').modal('show');
       });
}


function submitRemoveProductFromCart(){
    $(document).on('click', '.modal-content .removeProduct', function(event) {
        event.preventDefault();
        let dto = populateInputFormRemoveCart();
        console.log(dto);

        $.ajax({
            method: "DELETE",
            url: `${url}/cart/deleteProduct`,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem('token')}`
            },
            data: JSON.stringify(dto),
            success: function(response) {
                localStorage.setItem('successMessage', "Successfully Delete Shipment");
                location.reload();
            },
            error: function(response) {
                if (response.responseJSON && response.responseJSON.message) {
                    alert(`Error: ${response.responseJSON.message}`);
                } else {
                    alert(`Ada error dengan status code: ${response.status}`);
                }
            }
        });
    });
}

function populateInputFormRemoveCart(){
    let dto = {
    buyerId: $('#buyerId').val(),
    productId:$('#productId').val(),
    shipmentId:$('#shipmentId').val(),
    }
    return dto;
}



