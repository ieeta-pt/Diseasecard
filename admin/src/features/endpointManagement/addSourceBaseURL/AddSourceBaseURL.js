import React, {useEffect, useState} from "react";
import FormAddSourceBaseURL from "./forms/FormAddSourceBaseURL";
import {useDispatch, useSelector} from "react-redux";
import {addSourceURL} from "../endpointManagementSlice";

export const AddSourceBaseURL = () => {
    const dispatch = useDispatch();
    const labels = []

    const submit = (values) => {
        let formData = new FormData(document.forms.namedItem("addSourceBaseURLForm"))
        dispatch(addSourceURL(formData))
    }

    return (
        <div>
            <FormAddSourceBaseURL onSubmit={submit} labels={labels}/>
        </div>
    )
}