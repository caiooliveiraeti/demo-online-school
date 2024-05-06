new Vue({
    el: '#app',
    data: {
        students: []
    },
    created() {
        this.getAllStudents();
    },
    methods: {
        async getAllStudents() {
            try {
                const response = await axios.get('/students');
                this.students = response.data;
            } catch (error) {
                console.error('Error fetching students:', error);
            }
        },
        editStudent(student) {
            window.location.href = `form.html?id=${student.id}`;
        },
        async deleteStudent(studentId) {
            if (confirm('Are you sure you want to delete this student?')) {
                try {
                    await axios.delete(`/students/${studentId}`);
                    this.getAllStudents();
                } catch (error) {
                    console.error('Error deleting student:', error);
                }
            }
        }
    }
});
