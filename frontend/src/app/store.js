import { configureStore } from '@reduxjs/toolkit'
import searchReducer from '../features/search/searchSlice'
import diseaseReducer from '../features/disease/diseaseSlice'

export default configureStore({
    reducer: {
        disease: diseaseReducer,
        search: searchReducer
    }
})