<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" isELIgnored="false"%>

<script type="text/ng-template" id="confirmModal">
    <div class="modal-header text-center">
        <h5 class="modal-title">Подтверждение</h5>
    </div>
    <div class="modal-body text-center">
        {{message}}
    </div>
    <div class="modal-footer text-center">
        <button class="btn btn-md btn-default" type="button" ng-click="ok()">Да</button>
        <button class="btn btn-md" type="button" ng-click="cancel()">Нет</button>
    </div>
</script>

<script type="text/ng-template" id="messageModal">
    <div class="modal-header text-center">
        <h5 class="modal-title">Сообщение</h5>
    </div>
    <div class="modal-body text-center">
        {{message}}
    </div>
    <div class="modal-footer text-center">
        <button class="btn btn-md btn-default" type="button" ng-click="ok()">Закрыть</button>
    </div>
</script>