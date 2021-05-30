import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import API from "../../../api/Api";

const initialState = {
    allResources: [],
    systemBuild: false,
    btnBuild: false
}


export const getAllResources = createAsyncThunk('listResources/getAllResources', async () => {
    return API.GET("getAllResources", '', [] ).then(res => {
        const allResources = res.data
        return {allResources}
    })
})


export const startSystemBuild = createAsyncThunk('listResources/startBuildSystem', async () => {
    return API.GET("startBuildSystem", '', [] ).then(res => {
        return res.data
    })
})


export const startUnbuildSystem = createAsyncThunk('listResources/startUnbuildSystem', async () => {
    return API.GET("startUnbuildSystem", '', [] ).then(res => {
        return res.data
    })
})


export const getSystemBuild = createAsyncThunk('listResources/getSystemBuild', async () => {
    return API.GET("getSystemBuild", '', [] ).then(res => {
        console.log("Get System Build: ")
        console.log(res.data)
        const build = res.data
        return {build}
    })
})


const systemStatusSlice = createSlice({
    name: 'systemStatus',
    initialState,
    reducers: {
        updateBtnBuild: (state, action) => {
            state.btnBuild = action.payload
        }
    },
    extraReducers: {
        [getAllResources.fulfilled]: (state, action) => {
            state.allResources = action.payload.allResources
        },
        [getSystemBuild.fulfilled]: (state, action) => {
            const status = action.payload.build

            let finalStatus = true

            if (status === 'Finished' || status === 'Inconsistent' || status === 'Building_Removal') finalStatus = false
            if (status.includes("Building_") ) state.btnBuild = true
            if (status.includes("Finished") || status.includes("BuildReady")) state.btnBuild = false

            state.systemBuild = finalStatus
        },
    }
})


export const getResources = state => state.systemStatus.allResources
export const getSystemBuildValue = state => state.systemStatus.systemBuild
export const getBtnBuild = state => state.systemStatus.btnBuild

export const { updateBtnBuild } = systemStatusSlice.actions

export default systemStatusSlice.reducer