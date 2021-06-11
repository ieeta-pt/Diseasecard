import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    alerts: [],
    status: {}
}


export const getAlertBoxResults = createAsyncThunk('alertBox/getAlertBoxResults', async () => {
    return API.GET("getAlertBoxResults", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})



const alertBoxSlice = createSlice({
    name: 'alertBox',
    initialState,
    reducers: {

    },
    extraReducers: {
        [getAlertBoxResults.fulfilled]: (state, action) => {
            console.log("Alert Box Results: ")
            console.log(action.payload.results)
            state.alerts = action.payload.results.list
            state.status = action.payload.results.status
        }
    }
})


export const getListAlertBoxResults = state => state.alertBox.alerts
export const getAlertBoxStatus = state => state.alertBox.status

export const { storeEditRow } = alertBoxSlice.actions

export default alertBoxSlice.reducer