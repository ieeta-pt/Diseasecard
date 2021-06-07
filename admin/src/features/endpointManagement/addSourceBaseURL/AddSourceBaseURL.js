import React, {useEffect, useState} from "react";
import FormAddSourceBaseURL from "./forms/FormAddSourceBaseURL";
import {useDispatch, useSelector} from "react-redux";
import {addSourceURL, getLabelsBaseURLS, getResourcesWithoutBaseURL} from "../endpointManagementSlice";

export const AddSourceBaseURL = () => {
    const dispatch = useDispatch();
    const labels = useSelector(getLabelsBaseURLS)

    useEffect( () => {
        dispatch(getResourcesWithoutBaseURL())
    }, [])

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