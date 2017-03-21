	.text
	.globl main
main:
	movq $1, %rdi
	cmpq $0, %rdi
	je L24
	movq $1, %rdi
	cmpq $0, %rdi
	je L24
	movq $1, %rdi
L22:
	addq $65, %rdi
	call putchar
	movq $1, %rdi
	cmpq $0, %rdi
	je L16
	movq $2, %rdi
	cmpq $0, %rdi
	je L16
	movq $1, %rdi
L14:
	addq $65, %rdi
	call putchar
	movq $1, %rdi
	cmpq $0, %rdi
	je L8
	movq $0, %rdi
	cmpq $0, %rdi
	je L8
	movq $1, %rdi
L6:
	addq $65, %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	ret
L8:
	movq $0, %rdi
	jmp L6
L16:
	movq $0, %rdi
	jmp L14
L24:
	movq $0, %rdi
	jmp L22
	.data
