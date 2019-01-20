var row;
$(function () {
    $("#tableData").bootstrapTable({
        url : "/queryJob",
        clickToSelect: true,    	//是否启用点击选中行
        columns: [
            {field: 'selectItem', radio: true},
            {title: '序号',align: 'center', width:50, formatter:function (value,row,index) {
                    return index+1;
                }
            },
            {title: '任务名称', field: 'jobName', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '任务所在组', field: 'jobGroup', visible: true, align: 'center', valign: 'middle'},
            {title: '任务类名', field: 'jobClassName', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '触发器名称', field: 'triggerName', visible: false, align: 'center', valign: 'middle',width:180},
            {title: '触发器所在组', field: 'triggerGroup', visible: false, align: 'center', valign: 'middle'},
            {title: '表达式', field: 'cronExpression', visible: true, align: 'center', valign: 'middle',width:180},
            {title: '时区', field: 'timeZoneId', visible: true, align: 'center', valign: 'middle',width:180}
        ]
    })
})
function openInsert() {
    layer.open({
        type: 2,
        title: '添加定时任务',
        area: ['80%', '80%'],
        fixed: false, //不固定
        maxmin: true,
        content: 'view/insert.html',
        success: function (index, e) {
            $(':focus').blur();
        }
    });
}
function check() {
    var selected = $('#tableData').bootstrapTable('getSelections');
    if (selected.length == 0) {
        alert("请先选中表格中的某一记录！");
        return false;
    } else {
        row= selected[0];
        return true;
    }
}
function startJob() {
    if (check()) {
        $.ajax({
            url: '/start_job',
            type: "POST",
            data: {
                jobClassName: row.jobClassName,
                jobGroupName: row.jobGroup
            },
            success:function (data) {
                refresh();
                msg(data);
            }
        })
    }
}
function pauseJob() {
    if (check()) {
        $.ajax({
            url: '/pause_job',
            type: "POST",
            data: {
                jobClassName: row.jobClassName,
                jobGroupName: row.jobGroup
            },
            success:function (data) {
                refresh();
                msg(data);
            }
        })
    }
}
function deleteJob() {
    if (check()) {
        $.ajax({
            url: '/delete_job',
            type: "POST",
            data: {
                jobClassName: row.jobClassName,
                jobGroupName: row.jobGroup
            },
            success:function (data) {
                refresh();
                msg(data);
            }
        })
    }
}
function openUpdate() {
    if (check()) {
        layer.open({
            type: 1,
            title: '修改定时任务',
            area: ['80%', '80%'],
            fixed: false, //不固定
            maxmin: true,
            content: $("#insertWid"),
            success: function (index, e) {
                        $('#jobClassName').val(row.jobClassName);
                        $('#jobGroup').val(row.jobGroup);
                        $('#cronExpression').val(row.cronExpression);
                $(':focus').blur();
            }
        });
    }
}
function refresh() {
    $("#tableData").bootstrapTable("refresh")
}
function update() {
    $.ajax({
        url : '/update_job',
        type: 'POST',
        data:{
            jobGroupName : $("#jobGroup").val(),
            jobClassName : $("#jobClassName").val(),
            cronExpression : $("#cronExpression").val()
        },
        success :function (data) {
            refresh();
            back();
            msg(data);
        }
    })
}
function back() {
    layer.closeAll();
}
function msg(msg) {
    layer.msg(msg, {
        time: 2000, //2s后自动关闭
        icon: 1,
     });
}