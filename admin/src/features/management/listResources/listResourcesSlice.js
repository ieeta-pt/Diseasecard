import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import API from "../../../api/Api";

const initialState = {
    url: '',
    entities: [],
    ontologyStructure: [],
    editRow: []
}


export const getAllEntities = createAsyncThunk('listResources/getAllEntitiesInfo', async () => {
    return API.GET("getAllEntitiesInfo", '', [] ).then(res => {
    })
})


export const getOntologyStructureInfo = createAsyncThunk('listResources/getOntologyStructureInfo', async () => {
    return API.GET("getOntologyStructureInfo", '', [] ).then(res => {
        const ontologyStructure = res.data
        return { ontologyStructure }
    })
})


const listResourceSlice = createSlice({
    name: 'listResources',
    initialState,
    reducers: {
        storeEditRow: (state, action) => {
            if (action.payload.typeOf === "Entity") {
                let isEntityOfLabel = []
                action.payload.isEntityOf.map((key) => { isEntityOfLabel.push(key.label) })
                state.editRow = Object.assign({isEntityOfLabel: isEntityOfLabel}, action.payload);
            }
            else if (action.payload.typeOf === "Concept") {
                let relatedResourceLabel = []
                action.payload.hasResource.map((key) => { relatedResourceLabel.push(key.label) })
                state.editRow = Object.assign({relatedResourceLabel: relatedResourceLabel}, action.payload);
                state.editRow = Object.assign({extendedEntityLabel: action.payload.hasEntity.replace("http://bioinformatics.ua.pt/diseasecard/resource/","").toLowerCase()}, state.editRow);
            }
            else if (action.payload.typeOf === "Resource") {
                if (action.payload.publisher === "plugin") { state.editRow = Object.assign({publisherLabel: "OMIM"}, action.payload);}
                else {                                       state.editRow = Object.assign({publisherLabel: action.payload.publisher.toUpperCase()}, action.payload);}
            }


        }
    },
    extraReducers: {
        [getAllEntities.pending]: (state, action) => {
            /*state.status = 'loading'*/
        },
        [getAllEntities.fulfilled]: (state, action) => {
            /*state.invalidEndpoints = action.payload.invalidEndpoints
            state.url = action.payload.url
            state.protection = action.payload.protection
            state.status = 'succeeded'*/
        },
        [getAllEntities.rejected]: (state, action) => {
            /*state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message*/
        },
        [getOntologyStructureInfo.fulfilled]: (state, action) => {
            //console.log(action)
            state.ontologyStructure = action.payload.ontologyStructure
        },
    }
})


export const getOntologyStructure = state => state.listResources.ontologyStructure
export const getEditRow = state => state.listResources.editRow

export const { storeEditRow } = listResourceSlice.actions

export default listResourceSlice.reducer
