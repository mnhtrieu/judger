$(document).ready(function(){
    $('#addTest').click(function () {
        var tests = $('#tests').children('.row').children()
        var collapsed = []
        //------------------------------
        tests.each(function(){
            collapsed.push($(this).find('.card-test').hasClass('collapsed'))
        })
        var url = /*[[@{/admin/problems/addtest/}]]*/""
        console.log(url)
        ajaxCall(url,collapsed)
        return false;
    })



        $('.collapse').on('hidden.bs.collapse',function () {
            var name = $(this).find('.nametest').val()
            $(this).prev().find('.additional-info').text('- ' + (name.length <= 0 ? 'Test bez jména' : name))
        }).on('show.bs.collapse',function(){
            $(this).prev().find('.additional-info').text('')
        })

        $(".custom-checkbox").change(function () {
            var val = $(this).children('input[type="checkbox"]').is(":checked")
            var parent = $(this).closest('.row')
            parent.children('.col-of-data').toggleClass('col-sm-10').toggleClass('col-sm-6')
            parent.children('.col-of-output').toggleClass('d-none')
        })

        $(".test-panel").sortable({
            placeholder: '<div class="placeholder col-sm-12"></div>',
            containerSelector: 'div.test-panel',
            itemSelector: 'div.test-drag',
            onDrop: function ($item, container, _super, event) {
                $item.removeClass(container.group.options.draggedClass).removeAttr("style")
                $("body").removeClass(container.group.options.bodyClass)
                console.log(container)
                $('.test-panel').children().each(function(idx){
                    $(this).find("[name^=testCases]").each(function(i){
                        $(this).attr('name',$(this).attr('name').replace(/\[.\]/,'[' + idx + ']'));
                    })

                    $(this).find("[id^=testCases]").each(function(i){
                        $(this).attr('id',$(this).attr('id').replace(/testCases./,'testCases' + idx));
                    })
                    $(this).find("[data-id]").each(function(i){
                        $(this).attr('data-id',idx)
                    })
                })
            }

        });
})

function ajaxCall(url,collapsed){
    $.ajax({
        url: url, type: 'POST', data: $("#form").serialize(),
        success: function(data){
            var tests = $('#tests').children('.row').children()
            $("#resultBlock").html(data)
            loadTestElements()
            tests.each(function(i){
                if(collapsed[i] === true) {
                    var tmp = $('#test' + i)
                    tmp.toggleClass('show').prev().toggleClass('collapsed')
                    var name = tmp.find('.nametest').val()
                    tmp.prev().find('.additional-info').text('- ' + (name.length <= 0 ? 'Test bez jména' : name))
                }
            })
        }
    })
}

function loadTestElements() {
    $('.removeTest').click(function () {

        var id = $(this).parent().attr('data-id')
        var tests = $('#tests').children('.row').children()
        var collapsed = []
        //------------------------------
        tests.each(function () {
            var tmp = $(this).find('.card-body').attr('data-id')
            if (tmp == id) return true;
            collapsed.push($(this).find('.card-test').hasClass('collapsed'))
        })
        //------------------------------
        var url = /*[[@{/admin/problems/removetest/}]]*/""
        ajaxCall(url + id, collapsed)

        return false;
    })

    $(".file").each(function () {
        var cm = CodeMirror.fromTextArea($(this)[0], {
            mode: "shell",
            lineNumbers: true,
            lineWrapping: false,
            fixedGutter: false
        });
        cm.setSize(null, cm.defaultTextHeight() + 100)
        $(this).parents(".row").find('.run-test').click(function () {
            var url = /*[[@{/admin/problems/run/generator/}]]*/""
            var id = $(this).attr('data-id')
            var outID = $("div#output" + id + " > p")
            var str = cm.getValue()
            var btn = $(this)
            if (str.length <= 0) {
                outID.text("Error: the code is empty.")
                return;
            }
            btn.attr('disabled', true)
            $.ajax({
                url: url,
                type: 'POST',
                data: {source_code: cm.getValue()},
                success: function (data) {
                    if (data.stderr !== null) {
                        outID.text(data.stderr)
                    } else outID.text(data.stdout)
                    btn.attr('disabled', false)
                }
            })
        })
    })
}