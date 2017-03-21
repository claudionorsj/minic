	.text
	.globl main
main:
	movq $104, %rdi
	call putchar
	movq $101, %rdi
	call putchar
	movq $8, %rdi
	addq $100, %rdi
	call putchar
	movq $216, %rdi
	movq $2, %rcx
	movq %rdi, %rax
	cqto
	idivq %rcx
	movq %rax, %rdi
	call putchar
	movq $111, %rdi
	call putchar
	movq $32, %rdi
	call putchar
	movq $-1, %rcx
	movq $0, %rdi
	subq %rcx, %rdi
	addq $118, %rdi
	call putchar
	movq $122, %rdi
	movq $11, %rcx
	movq %rdi, %rax
	cqto
	idivq %rcx
	movq %rax, %rdi
	addq $100, %rdi
	call putchar
	movq $1, %rdi
	cmpq $2, %rdi
	jl L25
	movq $0, %rdi
L24:
	addq $113, %rdi
	call putchar
	movq $2, %rdi
	cmpq $1, %rdi
	jl L19
	movq $0, %rdi
L18:
	addq $108, %rdi
	call putchar
	movq $2, %rdi
	subq $2, %rdi
	cmpq $0, %rdi
	je L12
	movq $0, %rdi
L11:
	addq $99, %rdi
	call putchar
	movq $1, %rdi
	subq $2, %rdi
	cmpq $0, %rdi
	je L5
	movq $0, %rdi
L4:
	addq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L5:
	movq $1, %rdi
	jmp L4
L12:
	movq $1, %rdi
	jmp L11
L19:
	movq $1, %rdi
	jmp L18
L25:
	movq $1, %rdi
	jmp L24
	.data
